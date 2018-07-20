mat3 Kx = mat3(
			-1.0, 0.0, 1.0,
			-2.0 ,0.0, 2.0,
			-1.0, 0.0, 1.0
			);
	mat3 Ky = mat3(
			1.0, 2.0, 1.0,
			0.0, 0.0, 0.0,
			-1.0, -2.0, -1.0
			);
   float Lx = 0.0;
   float Ly = 0.0;
  
   for (int y = -1; y<=1; ++y)
   {
   	for(int x = -1; x<=1; ++x)
   	{
   		vec2 offset = vec2(v_TexCoordinate.x + float(x)*u_gPixelOffset.x, v_TexCoordinate.y + float(y)*u_gPixelOffset.y);
   		vec4 pixel = texture2D(u_Texture, offset);

   		float luminance = 0.3*pixel.r + 0.59*pixel.g + 0.11*pixel.b;
   		
   		Lx += luminance * Kx[y+1][x+1];
   		Ly += luminance * Kx[y+1][x+1];
   	}
   }
   float L = sqrt((Lx*Lx)+(Ly*Ly));
   gl_FragColor = vec4(L, L, L, 1);