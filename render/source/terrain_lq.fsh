# version 410 core

in VS_OUT {
    vec3 wld_normal;
    vec2 txt_coords;
    vec3 txt_blends;
    float dist_to;
} fs_in;


out vec4 out_color;


layout(std140) uniform Sun {
    vec3 direction;
    float intensity;
    float ambient;
} sun;

uniform sampler2D texture_n;
uniform sampler2D texture_r;
uniform sampler2D texture_g;
uniform sampler2D texture_b;

uniform float fog_near;
uniform float fog_delta;

float calculateFogAlpha(float dist) {
    
    return clamp((fog_near - dist) / fog_delta, 0.0, 1.0);
    
}

vec4 blendTextures(sampler2D sampler_n, sampler2D sampler_r, sampler2D sampler_g, sampler2D sampler_b) {
    
    vec4 n_color = texture(sampler_n, fs_in.txt_coords);
    vec4 r_color = texture(sampler_r, fs_in.txt_coords);
    vec4 g_color = texture(sampler_g, fs_in.txt_coords);
    vec4 b_color = texture(sampler_b, fs_in.txt_coords);
    
    n_color = mix(n_color, r_color, fs_in.txt_blends.r);
    n_color = mix(n_color, g_color, fs_in.txt_blends.g);
    n_color = mix(n_color, b_color, fs_in.txt_blends.b);
    
    return n_color;
    
}

void main(void) {
    
    vec4 sampled_colors = blendTextures(texture_n, texture_r, texture_g, texture_b);
    
    float diffuse_factor = dot(fs_in.wld_normal, -sun.direction);
    diffuse_factor = max(diffuse_factor, sun.ambient);
    
    out_color = diffuse_factor * sampled_colors;
    out_color.a = calculateFogAlpha(fs_in.dist_to);
    
}
