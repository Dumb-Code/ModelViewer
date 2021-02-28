#version 330 core

#include "terminalvelocitycabbage:materials.frag";
#include "terminalvelocitycabbage:lights_base.frag";
#include "terminalvelocitycabbage:directional_lights.frag";

in vec2 vertTextureCoord;
in vec3 vertVertexNormal;
in vec3 vertVertexPosition;

out vec4 fragColor;

uniform DirectionalLight directionalLight;

void main() {
   setupReflectivity(material, vertTextureCoord);
   setupColors(material, vertTextureCoord);
   //the color of the fragment multiplied by the ambient light
   vec4 color = materialAmbientColor * vec4(ambientLight, 1);
   color += calcDirectionalLight(directionalLight, vertVertexPosition, vertVertexNormal);
   if (color.a == 0) {
      discard;
   } else {
      fragColor = color;
   }
}
