#version 410 core

in vec3 txt_coords;

out vec4 out_color;

uniform samplerCube texture;

void main(void) {
    
    out_color = texture(texture, txt_coords);
    
}
