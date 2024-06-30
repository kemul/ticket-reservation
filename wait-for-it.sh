#!/usr/bin/env bash
#   Use this script to test if a given TCP host/port are available

TIMEOUT=15
QUIET=0
CHILD=0

usage()
{
    echo "Usage: $0 host:port [-t timeout] [-- command args]"
    echo "  -h HOST | --host=HOST       Host or IP under test"
    echo "  -p PORT | --port=PORT       TCP port under test"
    echo "  -t TIMEOUT | --timeout=TIMEOUT"
    echo "                              Timeout in seconds, zero for no timeout"
    echo "  -q | --quiet                Don't output any status messages"
    echo "  -- COMMAND ARGS             Execute command with args after the test finishes"
    exit 1
}

wait_for()
{
    if [[ ${TIMEOUT} -gt 0 ]]; then
        echo "Waiting ${TIMEOUT} seconds for ${HOST}:${PORT}"
    else
        echo "Waiting for ${HOST}:${PORT} without a timeout"
    fi
    start_ts=$(date +%s)
    while :
    do
        if [[ ${ISBUSY} -eq 1 ]]; then
            nc -z ${HOST} ${PORT}
            result=$?
        else
            (echo > /dev/tcp/${HOST}/${PORT}) >/dev/null 2>&1
            result=$?
        fi
        if [[ ${result} -eq 0 ]]; then
            end_ts=$(date +%s)
            echo "${HOST}:${PORT} is available after $((end_ts - start_ts)) seconds"
            break
        fi
        sleep 1
    done
    return 0
}

wait_for_wrapper()
{
    # In order to support SIGINT during timeout: http://unix.stackexchange.com/a/57692
    if [[ ${QUIET} -eq 1 ]]; then
        timeout ${TIMEOUT} $0 --quiet --child --host=${HOST} --port=${PORT} --timeout=${TIMEOUT} &
    else
        timeout ${TIMEOUT} $0 --child --host=${HOST} --port=${PORT} --timeout=${TIMEOUT} &
    fi
    PID=$!
    trap "kill -INT -$PID" INT
    wait $PID
    RESULT=$?
    if [[ ${RESULT} -ne 0 ]]; then
        echo "timeout occurred after waiting ${TIMEOUT} seconds for ${HOST}:${PORT}"
    fi
    return ${RESULT}
}

# process arguments
while [[ $# -gt 0 ]]
do
    case "$1" in
        *:* )
        hostport=(${1//:/ })
        HOST=${hostport[0]}
        PORT=${hostport[1]}
        shift 1
        ;;
        -h)
        HOST="$2"
        shift 2
        ;;
        --host=*)
        HOST="${1#*=}"
        shift 1
        ;;
        -p)
        PORT="$2"
        shift 2
        ;;
        --port=*)
        PORT="${1#*=}"
        shift 1
        ;;
        -t)
        TIMEOUT="$2"
        shift 2
        ;;
        --timeout=*)
        TIMEOUT="${1#*=}"
        shift 1
        ;;
        -q | --quiet)
        QUIET=1
        shift 1
        ;;
        --child)
        CHILD=1
        shift 1
        ;;
        --)
        shift
        break
        ;;
        -*)
        echo "Unknown option: $1"
        usage
        ;;
        *)
        echo "Unknown argument: $1"
        usage
        ;;
    esac
done

if [[ "${HOST}" == "" || "${PORT}" == "" ]]; then
    echo "Error: you need to provide a host and port to test."
    usage
fi

if [[ ${CHILD} -eq 1 ]]; then
    wait_for
    RESULT=$?
    exit ${RESULT}
else
    if [[ ${TIMEOUT} -gt 0 ]]; then
        wait_for_wrapper
        RESULT=$?
    else
        wait_for
        RESULT=$?
    fi
fi

if [[ $# -gt 0 ]]; then
    exec "$@"
else
    exit ${RESULT}
fi
