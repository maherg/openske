import openske.model.hardware.*
import openske.model.software.*
import openske.model.security.*

def router = new Router("router")
infra.add(router)

// Hosts and Software
500.times { hostCount ->
    def host = new Host("host-${hostCount}")
    router.addConnection(host)
    infra.add(host)
    // Software
    100.times { softCount ->
        def soft = new Software("cpe:/a:vendor-${softCount}:software-${softCount}:${softCount.toString()}", host)
        soft.addWeakness("CWE-285").addWeakness("CWE-400").addWeakness("CWE-707")
        infra.add(soft)
    }
}

// Users
50.times { userCount ->
    def user = new User("User ${userCount}", "user_${userCount}@user.com", router.getConnections().get(userCount), false)
    infra.add(user)
}

// Attackers
2.times { userCount ->
    def user = new User("Attacker ${userCount}", "attacker_${userCount}@attacker.com",router.getConnections().get(userCount), true)
    infra.add(user)
}
