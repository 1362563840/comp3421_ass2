out vec4 outputColor;

uniform vec4 input_color;

uniform mat4 view_matrix;

uniform mat4 model_matrix;

// Light properties
uniform vec3 lightPos;
uniform vec3 lightIntensity;
uniform vec3 ambientIntensity;

// Material properties
uniform vec3 ambientCoeff;
uniform vec3 diffuseCoeff;
uniform vec3 specularCoeff;
uniform float phongExp;

uniform sampler2D tex;

in vec4 viewPosition;
in vec3 m;

in vec2 texCoordFrag;


// for torchlight

// our codes
uniform float cutOff;

// distance formular factors
uniform float constant;
uniform float linear;
uniform float quadratic;

uniform int isDay;
uniform int flash_switch;

void main()
{
    // Compute the s, v and r vectors
    // change here, since the the light position is constant, in order
    // to calculate the new vector(parallel to the given source position/vector)
    // + should give the new vector
    // because the light vector is in local, so convert it to view
    vec4 newSourcePosition = view_matrix*vec4(lightPos,0) + viewPosition;
    vec3 s = normalize( newSourcePosition - viewPosition ).xyz;
    vec3 v = normalize(-viewPosition.xyz);
    vec3 r = normalize(reflect(-s,m));

    //
    vec3 s_flash = normalize(  vec4( 0 , 0 , 0 , 1 )  - viewPosition).xyz;
    vec3 v_flash = normalize(-viewPosition.xyz);
    vec3 r_flash = normalize(reflect(-s_flash,m));

    vec3 temp_vec3 = ( vec4( 0 , 0 , 0 , 1 )  - viewPosition ).xyz;
    float distance = length( temp_vec3 );
    float attenuation = 1.5 / ( constant + linear * distance +
    		    quadratic * (distance * distance));


    vec3 ambient = ambientIntensity*ambientCoeff;
    vec3 diffuse = max(lightIntensity*diffuseCoeff*dot(m,s), 0.0);
    vec3 specular;

    vec3 ambient_flash = ambientIntensity*ambientCoeff;
    vec3 diffuse_flash = max(lightIntensity*diffuseCoeff*dot(m,s_flash), 0.0);
    vec3 specular_flash;

    if ( isDay == 0 ) {
        ambient_flash = ambient_flash * attenuation;
        diffuse_flash = diffuse_flash * attenuation;
    }
    else {

    }

    // Only show specular reflections for the front face
    if (dot(m,s) > 0)
        specular = max(lightIntensity*specularCoeff*pow(dot(r,v),phongExp), 0.0);
    else
        specular = vec3(0.0,0.0,0.0);

    // Only show specular reflections for the front face
    if (dot(m,s_flash) > 0)
        specular_flash = max(lightIntensity*specularCoeff*pow(dot(r_flash,v_flash),phongExp), 0.0);
    else
        specular_flash = vec3(0.0,0.0,0.0);

    if( isDay == 0 ){
      specular_flash = specular_flash * attenuation;
    }

    vec4 ambientAndDiffuse = vec4(ambient + diffuse, 1.0);

    vec4 ambientAndDiffuse_flash = vec4(ambient_flash + diffuse_flash, 1.0);


    if ( isDay == 0 ) {
        if ( degrees( acos( dot( vec3( 0 , 0 , -1 ) , vec3( -s_flash.x , -s_flash.y , -s_flash.z ) ) ) ) <= cutOff ){

            // torch light is same as sunlight
            // but won't make effect in dayligh
            if ( flash_switch == 0 ){
              outputColor = 0.1 * ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
            }
            else {
              outputColor = ambientAndDiffuse_flash*input_color*texture(tex, texCoordFrag) + vec4(specular_flash, 1.0);

              outputColor = outputColor + 0.1 * ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
            }

        }
        else {
          // at dark, sunlight is only ten percent of daylight
          outputColor = 0.1 * ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
        }
    }
    else{
        outputColor = ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1.0);
    }

}
