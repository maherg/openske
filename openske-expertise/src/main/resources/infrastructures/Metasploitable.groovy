import openske.model.assets.data.FileAsset;
import openske.model.hardware.Host
import openske.model.security.User
import openske.model.security.UserAccount
import openske.model.software.Software


// Metasploitable VM
Host metasploitHost = new Host("192.168.56.101")
Software ubuntuLinux = new Software("cpe:/o:ubuntu:linux", metasploitHost)
UserAccount msfadminAccount = new UserAccount("msfadmin", "msfadmin", ubuntuLinux)

// Assets
FileAsset secretAsset = new FileAsset("Secret Plans", metasploitHost, "/home/secret/plans.xls")

// Attacker
Host attackerHost = new Host("192.168.1.44")
attackerHost.addConnection(metasploitHost)
User attacker = new User("Lone Attacker", "lone.attacker@example.com", attackerHost, true)
attacker.addAccount(msfadminAccount)

infra.add(metasploitHost)
infra.add(ubuntuLinux)
infra.add(msfadminAccount)
infra.add(secretAsset)
infra.add(attackerHost)
infra.add(attacker)
