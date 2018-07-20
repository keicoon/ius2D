precision mediump float;        // Set the default precision to medium. We don't need as high of a
                                // precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.

uniform int u_Type;				// the input Type

varying vec4 v_Color;           // This is the color from the vertex shader interpolated across the
                                // triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
 
// The entry point for our fragment shader.
void main()
{
    //gl_FragColor = (v_Color * texture2D(u_Texture, v_TexCoordinate));
    float f;
    if(u_Type == 0)
    	f = texture2D(u_Texture, v_TexCoordinate).r;
    else if(u_Type == 1)
    	f = texture2D(u_Texture, v_TexCoordinate).g;
    else if(u_Type == 2)
    	f = texture2D(u_Texture, v_TexCoordinate).b;
 	
 	if(f != 0.0)
		gl_FragColor = (v_Color * vec4(1.0, 1.0, 1.0, 1.0));
}