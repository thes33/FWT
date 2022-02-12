package  com.arboreantears.endlessworlds.game.test; 

import com.arboreantears.fwt.FWTController;
import com.arboreantears.fwt.XMLUICreator;
import com.arboreantears.fwt.components.FWTTextButton;
import com.arboreantears.fwt.components.FWTWindow;
import com.arboreantears.fwt.events.FWTButtonListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Test the creation of FWT components through XML alone. */ 
public class TestFWTXML extends TestingModule implements Runnable
{




	//******************************************************************************************************************
	// RUN
	//******************************************************************************************************************


	@Override
	public void oneShotTests(String[] args)
		{
			prepFWT();
		}


	@Override
	public void run()
		{



		}





	//******************************************************************************************************************
	// PREP COMPONENTS
	//******************************************************************************************************************


	/** Prep FWT components. */
	public void prepFWT()
		{
			Gdx.graphics.setWindowedMode(1377, 768);

			FWTWindow win1 = XMLUICreator.create("testingWindow1");
			FWTWindow win2 = XMLUICreator.create("testingWindow2");
			windowManager.addWindow(win1);
			windowManager.addWindow(win2);

			// Button Listener Modification
			FWTTextButton btn = ((FWTTextButton)win1.findComponent("txtbutton1"));
			btn.addButtonListener(new FWTButtonListener() {
				public void buttonPressed(int button)
					{
						System.out.println("Button Pressed!");
					}
			});


		}









	//******************************************************************************************************************
	// RENDERING
	//******************************************************************************************************************


	SpriteBatch spriteBatch;

	/** Render the test environment. */
	@Override
	public void render(SpriteBatch spriteBatch) 
		{			

			// ESCAPE -> Exit testing framework
			if (Gdx.input.isKeyPressed(Keys.ESCAPE)) 
				Gdx.app.exit();

			// REFRESH -> Refresh all FWT components
			if (Gdx.input.isKeyJustPressed(Keys.F5))
				{
					FWTController.log("Refreshing components...");
					FWTController.refresh();
				}
		}





}
