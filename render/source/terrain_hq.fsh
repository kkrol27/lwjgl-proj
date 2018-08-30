#version 410 core

in WLD_SPACE {
    vec3 normal;
    vec2 tangent;
    vec2 binormal;
} wld;

in TXT_SPACE {
    vec2 coords;
    vec3 blends;
} txt;

in float dist_to;

out vec4 out_color;

layout(std140) uniform Sun {
    vec3 direction;
    float intensity;
    float ambient;
} sun;

uniform sampler2D texture_n;
uniform sampler2D normals_n;
uniform sampler2D texture_r;
uniform sampler2D normals_r;
uniform sampler2D texture_g;
uniform sampler2D normals_g;
uniform sampler2D texture_b;
uniform sampler2D normals_b;

uniform float fog_near;
uniform float fog_delta;

float calculateFogAlpha(float dist) {
    
    return clamp((fog_near - dist) / fog_delta, 0.0, 1.0);
    
}

vec3 processNormal(vec4 n_color) {
    
    n_color.rg = 2.0 * n_color.rg - 1.0;
    
    vec3 u = vec3(wld.tangent, 0.0);
    vec3 v = wld.normal;
    vec3 w = vec3(0.0, wld.binormal);
    
    vec3 normal = n_color.r * u + n_color.b * v + n_color.g * w;
    normal = normalize(normal);
    
    return normal;
    
}

vec4 blendTextures(sampler2D sampler_n, sampler2D sampler_r, sampler2D sampler_g, sampler2D sampler_b) {
    
    vec4 n_color = texture(sampler_n, txt.coords);
    vec4 r_color = texture(sampler_r, txt.coords);
    vec4 g_color = texture(sampler_g, txt.coords);
    vec4 b_color = texture(sampler_b, txt.coords);
    
    n_color = mix(n_color, r_color, txt.blends.r);
    n_color = mix(n_color, g_color, txt.blends.g);
    n_color = mix(n_color, b_color, txt.blends.b);
    
    return n_color;
    
}

void main(void) {
    
    vec4 sampled_colors = blendTextures(texture_n, texture_r, texture_g, texture_b);
    vec4 sampled_normal = blendTextures(normals_n, normals_r, normals_g, normals_b);
    
    vec3 unit_normal = processNormal(sampled_colors);
    
    float diffuse_factor = sun.intensity * dot(unit_normal, -sun.direction);
    diffuse_factor = max(diffuse_factor, sun.ambient);
    
    out_color = diffuse_factor * sampled_colors;
    out_color.a = calculateFogAlpha(dist_to);
    
}
