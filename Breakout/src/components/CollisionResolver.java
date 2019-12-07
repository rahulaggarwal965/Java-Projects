package components;

import breakout.Breakout;
import breakout.BreakoutSound;
import libraries.Maths;
import threeDimensions.Vec2;

public class CollisionResolver {
	
	private ParticleSystem ps;
	private Breakout b;
	
	public CollisionResolver(Breakout b, ParticleSystem ps) {
		this.b = b;
		this.ps = ps;
	}
	
	public void resolveCollision(Paddle p, Ball b) {
		if(b.isAlive() && p.hitbox.intersects(b.hitbox)) {
			BreakoutSound.HIT.play(false);
			if(b.position.y + b.size.y - b.velocity.y < p.position.y) {
				float angle = Maths.map((b.position.x + b.size.x/2 - p.position.x)/p.size.x, 0, 1, 2.96f, 0.34f);
				b.setAngle(angle); //Top
			}
		}
	}
	
	public void resolveCollision(Ball bl, Brick br) {
		if(bl.isAlive() && br.isAlive() && bl.hitbox.intersects(br.hitbox)) {
			
			//Play Brick Breaking Sound
			BreakoutSound.EXPLOSION.play(false);
			
			//Handle Brick Death and Brick Count
			br.setAlive(false);
			this.b.setBrickCount(this.b.getBrickCount() - 1);
			
			
			//Change Ball Speed based on Brick Level
			bl.setSpeed(Ball.initialSpeed * (-0.25f* br.getLevel() + 2));
			
			//Create a Particle Explosion
			ps.createExplosion(br.position.x, br.position.y, br.c);
			
			//Test Collision Side
			Vec2 ballPrevPosition = bl.getPreviousPosition();
			
			if(ballPrevPosition.y + bl.size.y < br.position.y || ballPrevPosition.y > br.position.y + br.size.y) {
				bl.position.copy(ballPrevPosition);
				bl.updateHitbox();	
				bl.setAngle(-bl.angle); // Top or Bottom
			} else {
				bl.position.copy(ballPrevPosition);
				bl.updateHitbox();
				bl.setAngle((float) Math.PI - bl.angle); // Left or Right
			}
		}
	}
	
	public void resolveCollision(Paddle p, PowerUp pu) {
		if(pu != null && pu.isAlive() && p.hitbox.intersects(pu.hitbox)) {
			BreakoutSound.POWERUP.play(false);
			pu.setLife(0);
			this.b.handlePowerUp(pu.getType());
		}
	}
}
