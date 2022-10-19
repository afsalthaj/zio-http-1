package zio.http.api

import zio.http.URL
import zio.stacktracer.TracingImplicits.disableAutoTrace // scalafix:ok;

final case class EndpointRegistry[-MI, +MO, +EP] private (private val map: Map[EndpointSpec[_, _], URL])
    extends APILocator  { self =>
  def locate(api: EndpointSpec[_, _]): Option[URL] = map.get(api)
}
object EndpointRegistry {
  def apply[MI, MO, EP](address: URL, spec: ServiceSpec[MI, MO, EP]): EndpointRegistry[MI, MO, EP] = {
    val map = spec.apis
      .foldLeft[Map[EndpointSpec[_, _], URL]](Map.empty) { case (map, api) =>
        map.updated(api, address)
      }

    new EndpointRegistry[MI, MO, EP](map)
  }
}
