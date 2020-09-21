float unpackVec3ToFloat(vec3 packed)
{
    float result = dot(packed, 1.0 / vec3(1.0, 256.0, 256.0 * 256.0));
    return result * (256.0 * 256.0 * 256.0) / (256.0 * 256.0 * 256.0 - 1.0);
}
