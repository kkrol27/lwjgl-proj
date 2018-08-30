#version 410 core

layout(location = 0) in vec3 mdl_vertex;
layout(location = 1) in vec2 mdl_coords;

out vec4 clip_vertex;
out vec3 wld_to_cam;
out vec2 txt_coords;
out float dist_to;

uniform mat4 proj_matrix;
uniform mat4 view_matrix;
uniform vec3 cam_position;
uniform vec2 ripple_offset;

void main(void) {
    
    txt_coords = mdl_coords + ripple_offset;
    
    wld_to_cam = cam_position - mdl_vertex;
    
    dist_to = length(wld_to_cam);
    
    clip_vertex = proj_matrix * view_matrix * vec4(mdl_vertex, 1.0);
    gl_Position = clip_vertex;
    
}
