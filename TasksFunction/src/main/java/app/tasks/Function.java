package app.tasks;


import io.micronaut.function.aws.proxy.payload2.APIGatewayV2HTTPEventFunction;

/** AWS Lambda handler для Micronaut HTTP (контроллеры работают как обычно). */
public class Function extends APIGatewayV2HTTPEventFunction { }