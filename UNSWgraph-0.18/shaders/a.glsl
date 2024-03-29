
// Incoming vertex position
in vec3 position;

// Incoming color
in vec4 color;

uniform mat4 model_matrix;

uniform mat4 view_matrix;

uniform mat4 proj_matrix;

out vec4 fragColor;

void main() {
	// The global position is in homogenous coordinates
    vec4 globalPosition = model_matrix * vec4(position, 1);

    // The position in camera coordinates
    vec4 viewPosition = view_matrix * globalPosition;

    // The position in CVV coordinates
    gl_Position = proj_matrix * viewPosition;

    fragColor = color;
}
