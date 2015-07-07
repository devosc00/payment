import models.ExecuteCommand
import play.api.{GlobalSettings, Application}

/**
 * Created by rmasal on 2015-07-03.
 */
object Global extends GlobalSettings{

  override
  def onStart(app: Application): Unit = {
    System.out.println("onStart");
//    ExecuteCommand.
    ExecuteCommand.runCMD();
    //    ExecuteCommand.mapTheDrive();
  }
}
