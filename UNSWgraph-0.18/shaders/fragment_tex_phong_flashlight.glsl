
out vec4 outputColor;

uniform vec4 input_color;

uniform mat4 view_matrix;

// -------------------------
// our codes
// Light properties
// light position is just camera position
// uniform vec3 lightPos;
// uniform float x;
// uniform float y;
// uniform float z;


uniform vec3 lightIntensity;
uniform vec3 ambientIntensity;
// -------------------------

// Material properties
uniform vec3 ambientCoeff;
uniform vec3 diffuseCoeff;
uniform vec3 specularCoeff;
uniform float phongExp;

uniform sampler2D tex;

in vec4 viewPosition;
in vec3 m;

in vec2 texCoordFrag;

// -------------------------
// our codes

uniform mat4 model_matrix;

uniform float cutOff;

// distance formular factors
uniform float constant;
uniform float linear;
uniform float quadratic;

uniform int isDay;
// -------------------------


void main()
{
    // Compute the s, v and r vectors

    // view_matrix*vec4(lightPos,1)
    vec3 s = normalize(  vec4( 0 , 0 , 0 , 1 )  - viewPosition).xyz;
    vec3 v = normalize(-viewPosition.xyz);
    vec3 r = normalize(reflect(-s,m));

    // ------------------------
    // our code
    vec3 temp_vec3 = ( vec4( 0 , 0 , 0 , 1 )  - viewPosition ).xyz;
    float distance = length( temp_vec3 );
    float attenuation = 1.5 / ( constant + linear * distance +
    		    quadratic * (distance * distance));
    // ------------------------

    vec3 ambient = ambientIntensity*ambientCoeff;
    vec3 diffuse = max(lightIntensity*diffuseCoeff*dot(m,s), 0.0);
    vec3 specular;

    // ------------------------
    // our code
    if ( isDay == 0 ) {
        ambient = ambient * attenuation;
        diffuse = diffuse * attenuation;
    }
    else {

    }

    // ------------------------

    // Only show specular reflections for the front face
    if (dot(m,s) > 0)
        specular = max(lightIntensity*specularCoeff*pow(dot(r,v),phongExp), 0.0);
    else
        specular = vec3( 0.0 , 0.0 , 0.0 );

    // ------------------------
    // our code
    specular = specular * attenuation;
    // ------------------------

    vec4 ambientAndDiffuse = vec4(ambient + diffuse, 1.0);


    if ( isDay == 0 ) {
        if ( degrees( acos( dot( vec3( 0 , 0 , -1 ) , vec3( -s.x , -s.y , -s.z ) ) ) ) <= cutOff ){
        // if ( degrees( acos( dot( normal_light , vec3( -s.x , -s.y , -s.z ) ) ) ) <= cutOff ){
            outputColor = ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
        }
        else {
          vec4 dark_ambientAndDiffuse = vec4( 0.05 , 0.05 , 0.05 , 1.0 );
          outputColor = dark_ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
        }
    }
    else{
        outputColor = ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
    }
    // -------------------------

}
