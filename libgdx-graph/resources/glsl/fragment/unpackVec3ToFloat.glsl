float unpackVec3ToFloat(vec3 packedValue)
{
    float packScale = u_cameraClipping.y;
    float result = dot(packedValue, 1.0 / vec3(1.0, 256.0, 256.0 * 256.0));
    return packScale * result * (256.0 * 256.0 * 256.0) / (256.0 * 256.0 * 256.0 - 1.0);
}
