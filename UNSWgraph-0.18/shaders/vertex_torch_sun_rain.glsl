// the light will just use phong direction

// common varibale
// Incoming vertex position
in vec3 position;
// Incoming normal
in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;
out vec4 viewPosition;
// Incoming texture coordinate, but mode "rain" deos not need it
in vec2 texCoord;
out vec3 m;

out vec2 texCoordFrag;

uniform int mode;

// mode 1 -> normal just light

// mode 2 -> torch night

// mode 3 -> rain
in vec3 init_position;
// Incoming color
in vec4 color;
uniform float time;
out vec4 fragColor;

// mode 4 -> sun rotate
// sun_2d is for sun without texture
uniform int sun_2d;

void main() {
    if ( mode == 1 ) {
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

    if ( mode == 2 ) {
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

    if ( mode == 3 ) {
		//Velocity is passed in as the position attribute
		vec3 velocity = position;

		vec3 pos = init_position + time * velocity; //+ vec3(0, 0.5*gravity*time*time, 0);
		// The global position is in homogenous coordinates
	    vec4 globalPosition = model_matrix * vec4(pos, 1);

	    // The position in camera coordinates
	    viewPosition = view_matrix * globalPosition;

	    // The position in CVV coordinates
	    gl_Position = proj_matrix * viewPosition;

	    fragColor = color;
	    // fragColor.a = 1 - time * speed_color;
    }

    // should only just draw sun without texture
    if ( mode == 4 ) {
        // The global position is in homogenous coordinates
        vec4 globalPosition = model_matrix * vec4(position, 1);

        // The position in camera coordinates
        viewPosition = view_matrix * globalPosition;

        // The position in CVV coordinates
        gl_Position = proj_matrix * viewPosition;
    }

}
