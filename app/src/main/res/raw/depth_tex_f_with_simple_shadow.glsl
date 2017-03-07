#version 100
//precision highp float;
precision highp float;

// The position of the light in eye space.
uniform vec3 uLightPos;       	
  
// Texture variables: depth texture, object texture
uniform sampler2D uShadowTexture;
uniform sampler2D uObjectTexture;
  
// from vertex shader - values get interpolated
varying vec3 vPosition;
varying vec2 vTexCoords;
varying vec3 vNormal;
  
// shadow coordinates
varying vec4 vShadowCoord;

//Calculate variable bias
float calcBias()
{
	float bias;

	vec3 n = normalize( vNormal );

	// Direction of the light (from the fragment to the light)
	vec3 l = normalize( uLightPos );

	// Cosine of the angle between the normal and the light direction,
	// clamped above 0
	//  - light is at the vertical of the triangle -> 1
	//  - light is perpendiular to the triangle -> 0
	//  - light is behind the triangle -> 0
	float cosTheta = clamp(dot(n, l), 0.0, 1.0);

 	bias = 0.0001 * tan(acos(cosTheta));
	bias = clamp(bias, 0.0, 0.01);

 	return bias;
}

//Simple shadow mapping
float shadowSimple() 
{ 
	vec4 shadowMapPosition = vShadowCoord / vShadowCoord.w;
	
	float distanceFromLight = texture2D(uShadowTexture, shadowMapPosition.st).z;

	//1.0 = not in shadow (fragmant is closer to light than the value stored in shadow map)
	//0.0 = in shadow
	return float(distanceFromLight > shadowMapPosition.z - calcBias());
}
  
void main()                    		
{        
	vec3 lightVec = uLightPos - vPosition;
	lightVec = normalize(lightVec);
   	
   	// Phong shading with diffuse and ambient component
	float diffuseComponent = max(0.0, dot(lightVec, vNormal));
	float ambientComponent = 0.3;
 		
 	// Shadow
   	float shadow = 1.0;

	//if the fragment is not behind light view frustum
	if (vShadowCoord.w > 0.0) {
		shadow = shadowSimple();
			
		//scale 0.0-1.0 to 0.2-1.0
		//otherways everything in shadow would be black
		shadow = (shadow * 0.8) + 0.2;
	}

	// Final output color with shadow and lighting
    vec4 color = vec4(texture2D(uObjectTexture, vTexCoords).rgb, 1.0);
    gl_FragColor = (color * (diffuseComponent + ambientComponent) * shadow);
}                                                                     	
