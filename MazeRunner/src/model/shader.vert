#version 410 compatibility

varying vec3 color;

uniform float diffuseIntensityModifier;

void main(){
	// Retrieves the position of the vertex in eye space by
	// multiplying the vertex in object space with the
	// modelview matrix and stores it in a 3D vertex
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
	
	// Retrieves the direction of the light and stores it in a
	// normalized 3D vector
	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
	
	// Retrieves the surface normal by multiplying the normal
	// by the normal matrix. If you don't use non-uniform scaling
	// operations you could also do: = gl_Normal.xyz
	vec3 surfaceNormal = (gl_NormalMatrix * gl_Normal).xyz;
	
	// Retrieves the intensity of the diffuse light by taking the dot-product of
	// the surface normal and the light direction vectors and stores the value in a scalar.
	// If the value is lower than 0, the light is messed up and we don't want
	// to show it
	float diffuseLightIntensity = diffuseIntensityModifier * max(0,dot(surfaceNormal, lightDirection));
	
	// Sets the color (which is passed to the fragment program) to the concatenation
	// of the material color and the diffuse light intensity.
	color.rgb = diffuseLightIntensity * gl_Color.rgb;
	
	// Adds ambient color to the color so even the darkest part equals ambientColor.
	color += gl_LightModel.ambient.rgb;
	
	
	// Calculates the direction of the reflectionDirection by using the method reflect, wich takes
	// the normalized direction from the light source to the surface as the 1st parameter,
	// and the normalized surface normal as the second. Since lightDirection points to
	// the direction of the light and not the surface, we need to negate it in order for 
	// the returned vector to be valid.
	vec3 reflectionDirection = normalize(reflect(-lightDirection,surfaceNormal));
	
	// Stores the dot=product of the surface normal and the direction of the reflection
	// in a scalar. Also checks if the value is negative. If so, the scalar is set to 0
	float specular = max(0.0,dot(surfaceNormal,reflectionDirection));
	
	if(diffuseLightIntensity != 0){
		// Enhances the specular scalar value by raising it to the exponent of the shininess.
		float fspecular = pow(specular,gl_FrontMaterial.shininess);
		// Adds the specular value to the color.
		color.rgb *= fspecular;
	}
	
	// Retrieves the position of the vertex in clip space my multiplying it by the modelviw-
	// projection matrix and stores it in the built-in output variable gl_Position.
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

}