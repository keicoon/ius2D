precision mediump float;        // Set the default precision to medium. We don't need as high of a
                                // precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.

varying vec4 v_Color;           // This is the color from the vertex shader interpolated across the
                                // triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
 
// The entry point for our fragment shader.
void main()
{
	vec4 normalColor = texture2D(u_Texture, v_TexCoordinate);
	//black&white
	float gray = 0.299*normalColor.r + 0.587*normalColor.g + 0.114*normalColor.b;
    gl_FragColor = vec4(gray, gray, gray, normalColor.a); 
    
    //shepia
    //gl_FragColor = vec4(0.393*normalColor.r + 0.769*normalColor.g + 0.189*normalColor.b,
    //					0.349*normalColor.r + 0.686*normalColor.g + 0.168*normalColor.b,
    //					0.272*normalColor.r + 0.534*normalColor.g + 0.131*normalColor.b,
    //					normalColor.a);
    
    //default
    //gl_FragColor = (v_Color * texture2D(u_Texture, v_TexCoordinate));
}