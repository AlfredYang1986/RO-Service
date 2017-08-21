package bminjection

import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import play.api.{Configuration, Environment}

class ROModules extends play.api.inject.Module {
    def bindings(env : Environment, conf : Configuration) = {
        val impl = new ROModuleImpl
        Seq(
            bind[DBTrait].toInstance(impl),
            bind[AuthTokenTrait].toInstance(impl)
        )
    }
}