#version 100

precision mediump float;

attribute vec4 a_Position;
uniform mat4 u_Matrix;

void main() {
    gl_Position = u_Matrix * a_Position;
}
