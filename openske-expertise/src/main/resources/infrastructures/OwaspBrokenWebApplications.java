import openske.model.assets.data.FileAsset;
import openske.model.hardware.Host
import openske.model.security.User
import openske.model.security.UserAccount
import openske.model.software.Software


// OWASPBWA VM
def owaspbwaHost = new Host("192.168.1.76")
def ubuntuLinux = new Software("cpe:/o:ubuntu:linux", metasploitHost)
def msfadminAccount = new UserAccount("msfadmin", "msfadmin", ubuntuLinux)

// Assets
def secretAsset = new FileAsset("Secret Plans", metasploitHost, "/home/secret/plans.xls")

// Attacker
def attackerHost = new Host("192.168.1.44")
attackerHost.addConnection(metasploitHost)
def attacker = new User("Lone Attacker", "lone.attacker@example.com", attackerHost, true)
attacker.addAccount(msfadminAccount)

infra.add(metasploitHost)
infra.add(ubuntuLinux)
infra.add(msfadminAccount)
infra.add(secretAsset)
infra.add(attackerHost)
infra.add(attacker)
