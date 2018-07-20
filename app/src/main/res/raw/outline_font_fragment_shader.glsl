precision mediump float;        // 정밀도 표현

uniform sampler2D u_Texture;    // The input texture.
uniform vec2 u_gPixelOffset;

varying vec4 v_Color;           // vertex로부터 넘겨받음
varying vec2 v_TexCoordinate;   // vertex로부터 넘겨받음
 
void main()
{
	mat3 K = mat3(
			1.0, 1.0, 1.0,
			1.0 ,0.0, 1.0,
			1.0, 1.0, 1.0
			);
	float L = 0.0;
	vec4 M = texture2D(u_Texture, v_TexCoordinate);
	
	for (int y = -1; y<=1; ++y)
   	{
	   	for(int x = -1; x<=1; ++x)
	   	{
	   		vec2 offset = vec2(v_TexCoordinate.x + float(x)*u_gPixelOffset.x, v_TexCoordinate.y + float(y)*u_gPixelOffset.y);
	   		vec4 pixel = texture2D(u_Texture, offset);

	   		L += pixel.a * K[y+1][x+1];
	   	}
  	}
	if(M.a == 0.0 && L > 1.0)
		gl_FragColor = vec4(0.812, 0.075, 0.224, 1.0); 
	else
		gl_FragColor = M;
}