package com.pillowdrift.drillergame.other;

// A type to define the data inside the mesh.
public class Vertex {
	// Vars
	public float _x, _y, _u, _v;

	// Create a new vertex.
	public Vertex(float x, float y,
				 	float u, float v) {
		_x = x;
		_y = y;
		_u = u;
		_v = v;
	}
	
	public Vertex cpy() {
		return new Vertex(_x, _y, _u, _v);
	}
}