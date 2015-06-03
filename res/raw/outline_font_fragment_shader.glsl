precision mediump float;        // Set the default precision to medium. We don't need as high of a
                                // precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
uniform vec2 Offset;
varying vec4 v_Color;           // This is the color from the vertex shader interpolated across the
                                // triangle per fragment.

varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
 
// The entry point for our fragment shader.
void main()
{
    // Multiply the color by the diffuse illumination level and texture value to get final output color.
    
	vec2 TexCoord = v_TexCoordinate.st;
	//vec2 Offset = 1.0 / textureSize2D(u_Texture, 0);
	
	vec4 n = texture2D(u_Texture, vec2(TexCoord.x, TexCoord.y - Offset.y));
	vec4 e = texture2D(u_Texture, vec2(TexCoord.x + Offset.x, TexCoord.y));
	vec4 s = texture2D(u_Texture, vec2(TexCoord.x, TexCoord.y + Offset.y));
	vec4 w = texture2D(u_Texture, vec2(TexCoord.x - Offset.x, TexCoord.y));

	vec4 TexColor = texture2D(u_Texture, TexCoord);
	float GrowedAlpha = TexColor.a;
	GrowedAlpha = mix(GrowedAlpha, 1.0, s.a);
	GrowedAlpha = mix(GrowedAlpha, 1.0, w.a);
	GrowedAlpha = mix(GrowedAlpha, 1.0, n.a);
	GrowedAlpha = mix(GrowedAlpha, 1.0, e.a);

	vec4 OutlineColorWithNewAlpha = vec4(1.0, 1.0, 0.0, 1.0);
	OutlineColorWithNewAlpha.a = GrowedAlpha;
	vec4 CharColor = TexColor * v_Color;

	gl_FragColor = mix(OutlineColorWithNewAlpha, CharColor, CharColor.a);
}