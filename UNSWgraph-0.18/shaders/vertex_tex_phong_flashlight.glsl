// A shader that performs Phong shading by interpolating the normals and
// coordinates of each vertex in camera coordinates.
//
// Note: This shader assumes there is no non-uniform scale in either the view
// or the model transform.

// Incoming vertex position
in vec3 position;

// Incoming normal
in vec3 normal;

// Incoming texture coordinate
in vec2 texCoord;

uniform mat4 model_matrix;

uniform mat4 view_matrix;

uniform mat4 proj_matrix;

out vec4 viewPosition;
out vec3 m;

out vec2 texCoordFrag;

out vec3 normal_light;

out vec4 last_column;

void main() {

	  // The global position is in homogenous coordinates
    vec4 globalPosition = model_matrix * vec4(position, 1);

    // The position in camera coordinates
    viewPosition = view_matrix * globalPosition;

    // The position in CVV coordinates
    gl_Position = proj_matrix * viewPosition;

    // Compute the normal in view coordinates
    m = normalize(view_matrix*model_matrix * vec4(normal, 0)).xyz;

    texCoordFrag = texCoord;

    vec4 first_column = view_matrix * vec4( 1 , 0 , 0 , 0 );
    float a = first_column.x;
    float c = first_column.z;

    normal_light = normalize( vec3( a , 0 , -c ) );


    //  after inverse, then get the last last_column,
    // it should be in gloabl palce not camera place
    last_column = inverse( view_matrix ) * vec4( 0 , 0 , 0 , 1 );
    // light_pos_inside = last_column.xyz;
}
