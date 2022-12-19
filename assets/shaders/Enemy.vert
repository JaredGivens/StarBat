attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform vec3 u_sun;
uniform vec3 u_eye;

varying vec2 v_texCoord0;
varying vec4 v_color;

// Shades a point light and returns its contribution
vec3 shadeSun( vec3 normal, vec3 eye, vec3 vertex_position) {

    // TODO: Implement this method
    vec3 d = u_sun - vertex_position;
    vec3 il = vec3(100.0) / (1.0 + dot(d,d)); //* light.color;

    vec3 l = normalize(d);

    vec3 r = reflect(l, normal);
    vec3 v = normalize(eye - vertex_position);

    vec3 id  = il * vec3(0.5, 0.5, 0.5) * max(dot(normal, l), 0.0); 
    vec3 is = il * vec3(0.5, 0.5, 0.5) * pow(max(dot(v, r), 0.0), 0.5);
    return  id + is;

}
void main() {
	vec3 world_norm = normalize(vec3(u_worldTrans * vec4(a_normal, 0.0)));
	vec3 world_pos = vec3(u_worldTrans * vec4(a_position, 0.0));
	vec3 color = vec3(0.0, 0.0, 0.0);
	color += shadeSun(world_norm, u_eye, world_pos);
	v_color = vec4(color, 1.0);
	gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
	v_texCoord0 = a_texCoord0;
}
