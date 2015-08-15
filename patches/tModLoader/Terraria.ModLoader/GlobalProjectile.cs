using System;
using Terraria;

namespace Terraria.ModLoader
{
	public class GlobalProjectile
	{
		public Mod mod
		{
			get;
			internal set;
		}

		public string Name
		{
			get;
			internal set;
		}

		public virtual bool Autoload(ref string name)
		{
			return mod.Properties.Autoload;
		}

		public virtual void SetDefaults(Projectile projectile)
		{
		}

		public virtual bool PreAI(Projectile projectile)
		{
			return true;
		}

		public virtual void AI(Projectile projectile)
		{
		}

		public virtual void PostAI(Projectile projectile)
		{
		}
	}
}
