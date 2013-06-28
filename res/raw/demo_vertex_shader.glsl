uniform mat4 uProjectionM;
attribute vec4 aPosition;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;
void main() {
    //gl_Position = uProjectionM * vec4(aPosition, 1.0);
    gl_Position = uProjectionM * aPosition;
    vTextureCoord = aTextureCoord;
}