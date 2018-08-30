#version 410 core

layout(location = 0) in vec3 mdl_vertex;
layout(location = 1) in vec3 mdl_normal;
layout(location = 2) in vec2 mdl_coords;
layout(location = 3) in vec3 mdl_blends;

out VS_OUT {
    vec3 wld_normal;
    vec2 txt_coords;
    vec3 txt_blends;
    float dist_to;
} vs_out;

uniform mat4 view_matrix;
uniform mat4 proj_matrix;
uniform vec4 clip_plane;

void main(void) {
    
    vs_out.wld_normal = mdl_normal;
    
    vs_out.txt_coords = mdl_coords;
    
    vs_out.txt_blends = mdl_blends;
    
    vec4 wld_vertex = vec4(mdl_vertex, 1.0);
    
    gl_ClipDistance[0] = dot(clip_plane, wld_vertex);
    
    vec4 cam_vertex = view_matrix * wld_vertex;
    
    vs_out.dist_to = length(cam_vertex);
    
    gl_Position = proj_matrix * cam_vertex;
    
}
