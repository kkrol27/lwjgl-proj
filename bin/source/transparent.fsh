#version 410 core

in VS_OUT {
    vec3 wld_normal;
    vec2 txt_coords;
} fs_in;

out vec4 out_color;

uniform sampler2D texture;

void main(void) {
    
    out_color = texture(texture, txt_coords);
    
    if(out_color.a < 0.1) {
        discard;
    }
    
}
