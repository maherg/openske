import com.openske.model.assets.AssetAccessType;
import com.openske.model.assets.data.DatabaseAsset;
import com.openske.model.assets.data.FileAsset;
import com.openske.model.assets.services.ServiceAsset;
import com.openske.model.hardware.Host;
import com.openske.model.hardware.Router;
import com.openske.model.security.User;
import com.openske.model.security.UserAccount;
import com.openske.model.software.Software;
import com.openske.model.software.Weakness;
import com.openske.model.assets.data.*;

// Hosts
Host webHost = new Host("web.proggy");
Host dbHost = new Host("database.proggy");
Host station1 = new Host("station1.proggy");
Host station2 = new Host("station2.proggy");
Host attackerHost = new Host("attacker.proggy");
Router router = new Router("router.proggy");
// Connections
router.addConnection(webHost);
router.addConnection(dbHost);
router.addConnection(station1);
router.addConnection(station2);
webHost.addConnection(attackerHost);
// Software
Software phpMyAdmin = new Software("phpmyadmin", "phpmyadmin", "2.1", webHost);
Software apache = new Software("apache", "apache", "2.2", webHost);
Software mysql = new Software("mysql", "mysql", "4.1", dbHost); //.addVulnerability("CVE-2003-1480");
Software proggyWeb = new Software("proggysolutions", "proggyweb", "1.0", webHost);
webHost.addSoftware(apache);
webHost.addSoftware(phpMyAdmin);
webHost.addSoftware(proggyWeb);
dbHost.addSoftware(mysql);
// Weaknesses
Weakness cwe20 = new Weakness("CWE-20", proggyWeb);
Weakness cwe285 = new Weakness("CWE-285", proggyWeb);
Weakness cwe521 = new Weakness("CWE-521", phpMyAdmin);
Weakness cwe400 = new Weakness("CWE-400", proggyWeb);
// Assets
ServiceAsset proggyBookWeb = new ServiceAsset("Proggy Web", proggyWeb);
DatabaseAsset proggyBookDatabase = new DatabaseAsset("ProggyBook Database", dbHost, "proggybook");
FileAsset proggyBookDesign = new FileAsset("ProggyBook Design", station1, "/home/proggy/design.png");
dbHost.addAsset(proggyBookDatabase);
webHost.addAsset(proggyBookWeb);
station1.addAsset(proggyBookDesign);
// Asset Accesses
proggyWeb.addAssetAccess(proggyBookWeb, AssetAccessType.READ_EXECUTE);
proggyWeb.addAssetAccess(proggyBookDatabase, AssetAccessType.READ_WRITE);
phpMyAdmin.addAssetAccess(proggyBookDatabase, AssetAccessType.READ);
// User Accounts
UserAccount phpMyAdminAccount = new UserAccount("admin", "admin", phpMyAdmin);
UserAccount rootAccount = new UserAccount("root", "root", mysql);
UserAccount proggyAdminAccount = new UserAccount("admin", "admin", proggyWeb);
// Users
User attacker = new User("Mr. X", "mr.x@attackers.net", attackerHost, true);
// Insertion
session.insert(router);
session.insert(webHost);
session.insert(dbHost);
session.insert(station1);
session.insert(station2);
session.insert(phpMyAdmin);
session.insert(cwe20);
session.insert(cwe285);
session.insert(cwe521);
session.insert(cwe400);
session.insert(apache);
session.insert(mysql);
session.insert(proggyWeb);
session.insert(proggyBookWeb);
session.insert(proggyBookDatabase);
session.insert(proggyBookDesign);
session.insert(phpMyAdminAccount);
session.insert(proggyAdminAccount);
session.insert(rootAccount);
session.insert(attacker);
