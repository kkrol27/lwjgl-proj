#version 410 core

in vec4 clip_vertex;
in vec3 wld_to_cam;
in vec2 txt_coords;
in float dist_to;


out vec4 out_color;


layout(std140) uniform Sun {
    vec3 direction;
    float intensity;
    float ambient;
} sun;

uniform sampler2D dudv_texture;
uniform sampler2D normals_texture;
uniform sampler2D refract_texture;
uniform sampler2D reflect_texture;
uniform sampler2D refract_depth;

uniform vec4 clear_color;

uniform float fog_near;
uniform float fog_delta;
uniform float blend_depth;
uniform float ripple_strength;
uniform float near_plane;
uniform float far_plane;
uniform float fresnel_damper;
uniform float specular_damper;
uniform float specular_reflectivity;

float calculateFogAlpha(float dist) {
    
    return clamp((fog_near - dist) / fog_delta, 0.0, 1.0);
    
}

float linearizeDepthBuffer(float depth) {
    
    return 2.0 * near_plane * far_plane / (far_plane + near_plane - (2.0 * depth - 1.0) * (far_plane - near_plane));
    
}

float calculateWaterDepth(vec2 texture_coords) {
    
    float distance_to_surface = linearizeDepthBuffer(gl_FragCoord.z);
    
    float distance_to_bottom = texture(refract_depth, texture_coords).r;
    distance_to_bottom = linearizeDepthBuffer(distance_to_bottom);
    
    return distance_to_bottom - distance_to_surface;
    
}

void main(void) {
    
    vec2 projective_coords = (clip_vertex.xy / clip_vertex.w) / 2.0 + 0.5;
    vec2 reflect_coords = vec2(projective_coords.x, 1.0 - projective_coords.y);
    vec2 refract_coords = vec2(projective_coords.x, projective_coords.y);
    
    float depth_factor = clamp(calculateWaterDepth(refract_coords) / blend_depth, 0.0, 1.0);
    
    vec2 distortion = ripple_strength * (2.0 * texture(dudv_texture, txt_coords).rg - 1.0);
    reflect_coords += distortion;
    refract_coords += distortion;
    
    vec4 reflect_color = texture(reflect_texture, reflect_coords);
    vec4 refract_color = texture(refract_texture, refract_coords);
    
    vec4 normals_color = texture(normals_texture, txt_coords);
    vec3 unit_normal = normalize(vec3(2.0 * normals_color.r - 1.0, 3.0 * normals_color.b, 2.0 * normals_color.g - 1.0));
    
    vec3 unit_wld_to_cam = normalize(wld_to_cam);
    
    float fresnel_factor = -unit_wld_to_cam.y;
    fresnel_factor = pow(fresnel_factor, 0.5);
    
    vec3 reflection = reflect(sun.direction, unit_normal);
    float specular_factor = dot(reflection, unit_wld_to_cam);
    specular_factor = specular_reflectivity * pow(specular_factor, specular_damper);
    
    out_color = mix(refract_color, clear_color, depth_factor);
    out_color = mix(out_color, reflect_color, fresnel_factor);
    out_color.rgb += specular_factor;
    out_color.a = clamp(depth_factor, 0.0, 1.0);    
    out_color.a *= calculateFogAlpha(dist_to);
    
}
