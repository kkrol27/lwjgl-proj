#version 410 core

layout(location = 0) in vec3 mdl_vertex;

out vec3 txt_coords;

uniform mat3 view_matrix;
uniform mat4 proj_matrix;

void main(void) {
    
    txt_coords = mdl_vertex;
    
    gl_Position = (proj_matrix * vec4(view_matrix * mdl_vertex, 1.0)).xyww;
    
}
