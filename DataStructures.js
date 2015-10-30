//Request Structure
{
    uuid: "Client generated UUID",
    domain: "Domain name defined in mapping",
    service: "Service name defined in mapping"
}

//Response Structure
{
    uuid: "Request UUID",
    type: "receipt" || "response",
    success: true || false,
    //If response
    response: Object,
    //If success === false
    error: {
        code: int, //For application use
        msg: String //UI friendly
    }
}