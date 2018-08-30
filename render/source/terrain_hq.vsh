#version 410 core

layout(location = 0) in vec3 mdl_vertex;
layout(location = 1) in vec3 mdl_normal;
layout(location = 2) in vec2 mdl_coords;
layout(location = 3) in vec3 mdl_blends;
layout(location = 4) in vec2 mdl_tangent;
layout(location = 5) in vec2 mdl_binormal;

out WLD_SPACE {
    vec3 normal;
    vec2 tangent;
    vec2 binormal;
} wld;

out TXT_SPACE {
    vec2 coords;
    vec3 blends;
} txt;

out float dist_to;

uniform mat4 view_matrix;
uniform mat4 proj_matrix;
uniform vec4 clip_plane;

void main(void) {
    
    wld.normal = mdl_normal;
    
    txt.coords = mdl_coords;
    
    txt.blends = mdl_blends;
    
    wld.tangent = mdl_tangent;
    
    wld.binormal = mdl_binormal;
    
    vec4 wld_vertex = vec4(mdl_vertex, 1.0);
    
    gl_ClipDistance[0] = dot(clip_plane, wld_vertex);
    
    vec4 cam_vertex = view_matrix * wld_vertex;
    
    dist_to = length(cam_vertex);
    
    gl_Position = proj_matrix * cam_vertex;
    
}
