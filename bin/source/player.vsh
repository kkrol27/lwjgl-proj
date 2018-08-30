#version 410 core

layout(location = 0) in vec3 mdl_vertex;
layout(location = 1) in vec3 mdl_normal;
layout(location = 2) in vec2 mdl_coords;

out vec2 txt_coords;

uniform mat4 modl_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;
uniform vec4 clip_plane;

void main(void) {
    
    txt_coords = mdl_coords;
    
    vec4 wld_vertex = modl_matrix * vec4(mdl_vertex, 1.0);
    
    gl_ClipDistance[0] = dot(clip_plane, wld_vertex);
    
    gl_Position = proj_matrix * view_matrix * wld_vertex;
    
}
