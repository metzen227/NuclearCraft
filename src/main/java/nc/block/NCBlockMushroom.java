package nc.block;

import static nc.config.NCConfig.mushroom_spread_rate;

import java.util.Random;

import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NCBlockMushroom extends BlockMushroom {
	
	public NCBlockMushroom() {
		super();
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (mushroom_spread_rate <= 0) {
			return;
		}
		
		int spreadTime = 400 / mushroom_spread_rate;
		if (spreadTime <= 0) {
			spreadTime = 1;
		}
		
		if (rand.nextInt(spreadTime) == 0) {
			int shroomCheck = 5;
			
			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
				if (world.getBlockState(blockpos).getBlock() == this) {
					shroomCheck--;
					if (shroomCheck <= 0) {
						return;
					}
				}
			}
			
			BlockPos newPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			
			for (int k = 0; k < 4; ++k) {
				if (world.isAirBlock(newPos) && canBlockStay(world, newPos, getDefaultState())) {
					pos = newPos;
				}
				newPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
			}
			
			if (world.isAirBlock(newPos) && canBlockStay(world, newPos, getDefaultState())) {
				world.setBlockState(newPos, getDefaultState(), 2);
			}
		}
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {}
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState iblockstate = worldIn.getBlockState(pos.down());
			return worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(iblockstate, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
		}
		return false;
	}
}
