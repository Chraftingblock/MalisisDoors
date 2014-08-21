/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.doors.door.movement;

import net.malisis.core.renderer.animation.transformation.Rotation;
import net.malisis.core.renderer.animation.transformation.Transformation;
import net.malisis.doors.door.Door;
import net.malisis.doors.door.DoorState;
import net.malisis.doors.door.tileentity.DoorTileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author Ordinastie
 * 
 */
public class FenceGateMovement implements IDoorMovement
{

	@Override
	public AxisAlignedBB getBoundingBox(DoorTileEntity tileEntity, boolean topBlock, boolean selBox)
	{
		//never called
		return null;
	}

	@Override
	public Transformation getTopTransformation(DoorTileEntity tileEntity)
	{
		return getTransformation(tileEntity, true);
	}

	@Override
	public Transformation getBottomTransformation(DoorTileEntity tileEntity)
	{
		return getTransformation(tileEntity, false);
	}

	public Transformation getTransformation(DoorTileEntity tileEntity, boolean left)
	{
		float hingeOffset = -0.5F + 0.125F / 2;
		boolean reversedOpen = ((tileEntity.getBlockMetadata() >> 1) & 1) == 1;
		int direction = tileEntity.getDirection();

		float fromAngle = 0, toAngle = 90;
		float hingeX = hingeOffset;
		float hingeZ = 0;
		if (direction == Door.DIR_NORTH || direction == Door.DIR_SOUTH)
		{
			reversedOpen = !reversedOpen;
			hingeX = 0;
			hingeZ = -hingeOffset;
		}

		if (!reversedOpen)
			toAngle = -90;
		if (tileEntity.getState() == DoorState.CLOSING || tileEntity.getState() == DoorState.CLOSED)
		{
			float tmp = fromAngle;
			fromAngle = toAngle;
			toAngle = tmp;
		}

		if (!left)
		{
			hingeX = -hingeX;
			hingeZ = -hingeZ;
			fromAngle = -fromAngle;
			toAngle = -toAngle;
		}

		return new Rotation(fromAngle, toAngle).aroundAxis(0, 1, 0).offset(hingeX, 0, hingeZ)
				.forTicks(tileEntity.getDescriptor().getOpeningTime());
	}
}
