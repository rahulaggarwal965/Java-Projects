package components;

import math.Collision;
import shooter.Shooter;
import shooter.ShooterSound;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Ray;
import threeDimensions.Vec3;

public class CollisionResolver {

	private ParticleSystem ps;
	private Shooter s;
	
	public CollisionResolver(Shooter s, ParticleSystem ps) {
		this.s = s;
		this.ps = ps;
	}
	
	public void resolveCollision(Player p, Enemy e) {
		Ray r = p.getRay();
		if(r != null && Collision.RayOBBIntersection(r.getRayOrigin(), r.getRayDirection(), e.bbMin, e.bbMax, e.getWorld())) {
			p.setRay(null);
			ShooterSound.HIT.play(false);
			e.setHealth(e.getHealth() -  5.0f);
			e.setBBColor(PackedColor.Green);
		}
		r = e.getRay();
		if(r != null && Collision.RayOBBIntersection(r.getRayOrigin(), r.getRayDirection(), p.bbMin, p.bbMax, p.getWorld())) {
			ShooterSound.HIT.play(false);
			e.setRay(null);
			p.setBBColor(PackedColor.Green);
			p.setHealth(p.getHealth() - 5.0f);
		}
	}
	
	public void resolveCollision(Missile m, Plane p) {
		float dist = m.getPosition()._subtract(p.getPosition()).magSq();
		if(dist < 4f) {
			ShooterSound.EXPLOSION.play(false);
			for (int i = 0; i < 30; i++) {
				this.ps.addParticle(Plane.pQuad, m.getPosition()._add(Vec3.random(5.0f)), Vec3.zero, 5f, p.getVelocity()._add(Vec3.random(50.0f)), 1.0f);
			}
		 	m.setLife(0.0f);
		 	p.setHealth(p.getHealth() - 10.0f);
		}
		
	}
}
