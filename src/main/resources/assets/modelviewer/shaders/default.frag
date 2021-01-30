#version 330 core

#include "terminalvelocitycabbage:materials.frag";
#include "terminalvelocitycabbage:lights_base.frag";
#include "terminalvelocitycabbage:point_lights.frag";
#include "terminalvelocitycabbage:spot_lights.frag";
#include "terminalvelocitycabbage:directional_lights.frag";

in vec2 vertTextureCoord;
in vec3 vertVertexNormal;
in vec3 vertVertexPosition;

out vec4 fragColor;

const int MAX_POINT_LIGHTS = 256;
const int MAX_SPOT_LIGHTS = 256;

uniform int pointLightsNum;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform int spotLightsNum;
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;

void main() {
   setupReflectivity(material, vertTextureCoord);
   setupColors(material, vertTextureCoord);
   //the color of the fragment multiplied by the ambient light
   vec4 color = materialAmbientColor * vec4(ambientLight, 1);
   color += calcDirectionalLight(directionalLight, vertVertexPosition, vertVertexNormal);
   for (int i = 0; i < pointLightsNum; i++) {
      if (pointLights[i].intensity > 0) {
         color += calcPointLight(pointLights[i], vertVertexPosition, vertVertexNormal);
      }
   }
   for (int i = 0; i < spotLightsNum; i++) {
      if (spotLights[i].intensity > 0) {
         color += calcSpotLight(spotLights[i], vertVertexPosition, vertVertexNormal);
      }
   }
   if (color.a == 0) {
      discard;
   } else {
      fragColor = color;
   }
}
