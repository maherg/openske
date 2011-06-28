import openske.model.assets.AssetAccessType;
import openske.model.assets.data.DatabaseAsset;
import openske.model.assets.data.FileAsset;
import openske.model.assets.services.ServiceAsset;
import openske.model.hardware.Host;
import openske.model.hardware.Router;
import openske.model.security.User;
import openske.model.security.UserAccount;
import openske.model.software.Software;
import openske.model.software.Weakness;
import openske.model.assets.data.*;
import openske.model.Infrastructure;

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
Software phpMyAdmin = new Software("cpe:/a:phpmyadmin:phpmyadmin:2.1", webHost);
Software apache = new Software("cpe:/a:apache:apache:2.2", webHost);
Software mysql = new Software("cpe:/a:mysql:mysql:4.1", dbHost); //.addVulnerability("CVE-2003-1480");
Software proggyWeb = new Software("cpe:/a:proggysolutions:proggyweb:1.0", webHost);
webHost.addSoftware(apache);
webHost.addSoftware(phpMyAdmin);
webHost.addSoftware(proggyWeb);
dbHost.addSoftware(mysql);

// Weaknesses
proggyWeb.addWeakness("CWE-20");
proggyWeb.addWeakness("CWE-285");
proggyWeb.addWeakness("CWE-400");
phpMyAdmin.addWeakness("CWE-521");

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
infra.add(router);
infra.add(webHost);
infra.add(dbHost);
infra.add(station1);
infra.add(station2);
infra.add(phpMyAdmin);
infra.add(apache);
infra.add(mysql);
infra.add(proggyWeb);
infra.add(proggyBookWeb);
infra.add(proggyBookDatabase);
infra.add(proggyBookDesign);
infra.add(phpMyAdminAccount);
infra.add(proggyAdminAccount);
infra.add(rootAccount);
infra.add(attacker);
