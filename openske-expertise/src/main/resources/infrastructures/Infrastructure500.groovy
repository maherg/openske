import com.openske.model.hardware.*
import com.openske.model.software.*
import com.openske.model.security.*

def router = new Router("router")
session.insert(router)

// Hosts and Software
500.times { hostCount ->
    def host = new Host("host-${hostCount}")
    router.addConnection(host)
    session.insert(host)
    // Software
    100.times { softCount ->
        def soft = new Software("cpe:/a:vendor-${softCount}:software-${softCount}:${softCount.toString()}", host)
        soft.addWeakness("CWE-285").addWeakness("CWE-400").addWeakness("CWE-707")
        session.insert(soft)
    }
}

// Users
50.times { userCount ->
    def user = new User("User ${userCount}", "user_${userCount}@user.com", router.getConnections().get(userCount), false)
    session.insert(user)
}

// Attackers
2.times { userCount ->
    def user = new User("Attacker ${userCount}", "attacker_${userCount}@attacker.com",router.getConnections().get(userCount), true)
    session.insert(user)
}
