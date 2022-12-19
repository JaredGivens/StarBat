#ifdef GL_ES 
precision mediump float;
#endif

in vec4 v_color;

varying vec2 v_texCoord0;

void main() {
	gl_FragColor = v_color;
}
