package com.arboreantears.fwt;

import java.util.HashMap;
import java.util.Stack;

import com.arboreantears.fwt.components.FWTComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;


/** A Buffer Controller to handle UI frame-buffer management. */
public class FWTUIFrameBufferController
{

	// INITIALIZATION
	//============================================

	/** Initializes and reset the frame buffers. */
	public static void initialize()
		{
			dispose();

			uiDrawingStack = new Stack<FrameBuffer>();
			uiBuffers = new HashMap<Long,FrameBuffer>();
		}



	/** Clears all frame buffer references. */
	public static void dispose()
		{
			if (uiDrawingStack != null)
				{
					while (!uiDrawingStack.isEmpty())
						uiDrawingStack.pop().dispose();
					uiDrawingStack = null;
				}

			if (uiBuffers != null)
				{
					for (FrameBuffer fb : uiBuffers.values())
						fb.dispose();
					uiBuffers.clear();
				}
			
			currentFrameBuffer = null;
		}











	// CURRENT RENDERING CONTEXT
	//============================================


	/** The current stack of drawing contexts (frame buffers). */
	private static Stack<FrameBuffer> uiDrawingStack;


	/** Starts drawing to the given frame buffer context. */
	public static void pushDrawingContext(FrameBuffer buffer)
		{
			// IF no buffer provided
			if (buffer == null)
				return; 

			// IF has current buffer
			if (currentFrameBuffer != null)
				// End previous buffer rendering
				currentFrameBuffer.end();

			// Add new buffer to stack
			uiDrawingStack.push(buffer);
			currentFrameBuffer = buffer;

			// Begin buffer rendering
			buffer.begin();

			// Set proper projection matrices
			FWTController.getSpriteBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,buffer.getWidth(),buffer.getHeight()));
			FWTController.getShapeRenderer().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,buffer.getWidth(),buffer.getHeight()));

		}


	/** Returns drawing to the previous frame buffer context. */
	public static FrameBuffer popDrawingContext()
		{
			// IF has no buffer, do nothing
			if (currentFrameBuffer == null)
				return null;

			// End previous buffer rendering
			currentFrameBuffer.end();

			// Remove buffer from stack
			FrameBuffer lastBuffer = uiDrawingStack.pop();

			// IF has another buffer on stack
			if (!uiDrawingStack.isEmpty())
				{
					// Get last buffer
					FrameBuffer buffer = uiDrawingStack.peek();
					currentFrameBuffer = buffer;
					// Return to last buffer rendering
					buffer.begin();
					// Set proper projection matrices
					FWTController.getSpriteBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,buffer.getWidth(),buffer.getHeight()));
					FWTController.getShapeRenderer().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,buffer.getWidth(),buffer.getHeight()));
				}
			else // ELSE back to main rendering
				{
					// Unbind buffers
					FrameBuffer.unbind();
					currentFrameBuffer = null;

					// Return to screen projection matrices
					FWTController.getSpriteBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
					FWTController.getShapeRenderer().setProjectionMatrix(new Matrix4().setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));

				}
			return lastBuffer;

		}


	/** Current frame-buffer accepting rendering commands. Null indicates the main screen is the current focus. */
	private static FrameBuffer currentFrameBuffer = null;

	/** Returns the current frame-buffer accepting rendering commands. Null indicates the main screen is the current focus. */
	public static FrameBuffer getCurrentFrameBuffer() {return currentFrameBuffer;}
	/** Returns 'true' if the default frame buffer is active. */
	public static boolean isScreenRendering() {return currentFrameBuffer == null;}


	
	
	
	
	
	
	




	// UI FRAME BUFFERS
	//============================================


	/** The collection of UI frame buffers for double-buffer rendering. */
	public static HashMap<Long,FrameBuffer> uiBuffers;


	/** Returns the frame buffer connected to the given UI component. If the buffer does not exist a new buffer is created. */
	public static FrameBuffer getUIBuffer(FWTComponent component)
		{
			FrameBuffer fb =  uiBuffers.get(component.getID());
			// IF does not exist
			if (fb == null)
				{
					fb = new FrameBuffer(Format.RGBA8888,(int)component.getDimensions().width,(int)component.getDimensions().height,false);
					uiBuffers.put(component.getID(), fb);
				}
			return fb;
		}


	/** Resizes the frame buffer connected to the given UI component based on its current width/height. */
	public static void resizeUIBuffer(FWTComponent component) 
		{
			resizeUIBuffer(component, (int)component.getDimensions().width,(int)component.getDimensions().height);
		}


	/** Resizes the frame buffer connected to the given UI component with the specified size. */
	public static void resizeUIBuffer(FWTComponent component, int width, int height) 
		{
			FrameBuffer fb =  uiBuffers.get(component.getID());
			// IF exists
			if (fb != null) // Remove old buffer
				{removeUIBuffer(component);}
			// Ensure safe
			if (width<=0 || height<=0)
				return;
			// Create new buffer
			fb = new FrameBuffer(Format.RGBA8888,width,height,false);
			uiBuffers.put(component.getID(), fb);

		}


	/** Removes the frame buffer connected to the given UI component. */
	public static void removeUIBuffer(FWTComponent component) 
		{
			FrameBuffer fb = uiBuffers.remove(component.getID());
			if (fb != null)
				fb.dispose();
		}




	
	
	
	
	

	
	




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



}
