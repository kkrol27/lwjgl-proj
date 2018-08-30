#version 410 core

in vec2 txt_coords;

out vec4 out_color;

uniform sampler2D texture_0;

void main(void) {
    
    out_color = vec4(texture(texture_0, txt_coords).rgb, 1.0);
    
}
