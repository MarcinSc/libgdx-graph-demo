vec3 packFloatToVec3(float value)
{
    float scale = value * (256.0 * 256.0 * 256.0 - 1.0) / (256.0 * 256.0 * 256.0);
    vec4 encode = fract(scale * vec4(1.0, 256.0, 256.0 * 256.0, 256.0 * 256.0 * 256.0));
    return encode.xyz - encode.yzw / 256.0 + 1.0/512.0;
}
