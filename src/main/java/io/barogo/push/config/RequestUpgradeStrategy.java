//package io.barogo.push.config;
//
//import static org.springframework.http.server.reactive.ServerHttpResponseDecorator.getNativeResponse;
//
//import java.util.function.Supplier;
//import org.springframework.core.io.buffer.NettyDataBufferFactory;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.reactive.socket.HandshakeInfo;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
//import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.server.HttpServerResponse;
//
//public class RequestUpgradeStrategy extends ReactorNettyRequestUpgradeStrategy {
//
//  @Override
//  public Mono<Void> upgrade(ServerWebExchange exchange, WebSocketHandler handler,
//      String subProtocol, Supplier<HandshakeInfo> handshakeInfoFactory) {
////    return super.upgrade(exchange, handler, subProtocol, handshakeInfoFactory);
//
//    ServerHttpResponse response = exchange.getResponse();
//    HttpServerResponse reactorResponse = getNativeResponse(response);
//    HandshakeInfo handshakeInfo = handshakeInfoFactory.get();
//    NettyDataBufferFactory bufferFactory = (NettyDataBufferFactory) response.bufferFactory();
//
//    var authResult = validateAuth(handshakeInfo);
//    if (authResult == unauthorised) return Mono.just(reactorResponse.status(rejectedStatus))
//        .flatMap(HttpServerResponse::send);
//    else return reactorResponse.sendWebsocket(subProtocol, //
//        this.maxFramePayloadLength,//
//        (in, out) -> {
//          ReactorNettyWebSocketSession session = new ReactorNettyWebSocketSession(in, out,
//              handshakeInfo,
//              bufferFactory,
//              this.maxFramePayloadLength);
//          return handler.handle(session);
//        });
//  }
//}
