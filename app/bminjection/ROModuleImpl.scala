package bminjection

import bminjection.db.MongoDB.MongoDBImpl
import bminjection.token.ROToken.ROTokenTrait

/**
  * Created by alfredyang on 01/06/2017.
  */
class ROModuleImpl extends ROTokenTrait with MongoDBImpl
