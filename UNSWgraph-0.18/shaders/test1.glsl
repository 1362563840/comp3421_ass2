
//------------------------------------------
// for rain

// in vec3 debug;

in vec3 init_position;

// Incoming color
in vec4 color;

uniform float time;

uniform float gravity;

out vec4 fragColor;

//------------------------------------------
// for direction

// Incoming normal
in vec3 normal;

// Incoming texture coordinate
in vec2 texCoord;

out vec4 viewPosition;
out vec3 m;
out vec2 texCoordFrag;

//------------------------------------------
// both

// Incoming vertex position
in vec3 position;

uniform mat4 model_matrix;

uniform mat4 view_matrix;

uniform mat4 proj_matrix;

uniform int rain;
//------------------------------------------





void main() {

	if ( rain == 1 ) {
		//Velocity is passed in as the position attribute
		vec3 velocity = position;

		vec3 pos = init_position + time * velocity; //+ vec3(0, 0.5*gravity*time*time, 0);

		// The global position is in homogenous coordinates
	    vec4 globalPosition = model_matrix * vec4(pos, 1);

	    // The position in camera coordinates
	    vec4 viewPosition = view_matrix * globalPosition;

	    // The position in CVV coordinates
	    gl_Position = proj_matrix * viewPosition;

	    fragColor = color;
	    fragColor.a = 1 - time*1;
	}
	else {
		// The global position is in homogenous coordinates
	    vec4 globalPosition = model_matrix * vec4(position, 1);

	    // The position in camera coordinates
	    viewPosition = view_matrix * globalPosition;

	    // The position in CVV coordinates
	    gl_Position = proj_matrix * viewPosition;

	    // Compute the normal in view coordinates
	    m = normalize(view_matrix*model_matrix * vec4(normal, 0)).xyz;

	    texCoordFrag = texCoord;
	}

}
