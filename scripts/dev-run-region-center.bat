@echo off
setlocal

cd /d "%~dp0.."

set "WORLD_MESH_REGION_ID=region-center"
set "WORLD_MESH_REGION_MIN_X=-10"
set "WORLD_MESH_REGION_MAX_X=10"
set "WORLD_MESH_REGION_MIN_Z=-10"
set "WORLD_MESH_REGION_MAX_Z=10"
set "WORLD_MESH_SPAWN_Y=42"

set "WORLD_MESH_NEIGHBOR_EAST_ID=region-east"
set "WORLD_MESH_NEIGHBOR_EAST_ENDPOINT=local-dev-east"

echo Starting WorldMesh dev region worker...
echo Region: %WORLD_MESH_REGION_ID%
echo Bounds: X %WORLD_MESH_REGION_MIN_X%..%WORLD_MESH_REGION_MAX_X%, Z %WORLD_MESH_REGION_MIN_Z%..%WORLD_MESH_REGION_MAX_Z%
echo East neighbor: %WORLD_MESH_NEIGHBOR_EAST_ID% @ %WORLD_MESH_NEIGHBOR_EAST_ENDPOINT%
echo.

call .\gradlew.bat :apps:region-worker:run

endlocal