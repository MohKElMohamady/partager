package io.icarus7.voyage.services;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import io.icarus7.partage.*;
import io.icarus7.voyage.models.*;
import io.icarus7.voyage.models.User;
import io.icarus7.voyage.models.Voyage;
import io.icarus7.voyage.repositories.VoyagesRepository;
import io.icarus7.voyage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@GrpcService
@Slf4j
public class VoyagesService extends VoyagesServiceGrpc.VoyagesServiceImplBase {
    @Autowired
    private VoyagesRepository voyagesRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void addANewVoyage(AddNewVoyageRequest request, StreamObserver<io.icarus7.partage.Voyage> responseObserver) {
        if(this.isTripValid(request).isPresent()){
            responseObserver.onError(this.isTripValid(request).get());
            responseObserver.onCompleted();
            return;
        }
        UUID tripId = UUID.randomUUID();
        // Hardcoded the user, should be fetched from context or token later on.
        var user = User.builder().userId("d51b3dcc-5f3e-11ed-8962-370039a9ba0e")
                .email("moh.k.elmohamady@protonmail.ch")
                .firstName("Mohamed")
                .lastName("Khaled")
                .build();
        var trip = Voyage.builder()
                .creator(user)
                .voyageId(tripId)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .departure(new Timestamp(request.getDeparture()))
                .description(request.getDescription())
                .maximumCapacity(request.getMaximumCapacity())
                .cost(BigDecimal.valueOf(request.getTripCost()))
                .status(VoyageStatus.OPEN)
                .build();
        this.usersRepository.save(user);
        this.voyagesRepository.save(trip);

        responseObserver.onNext(io.icarus7.partage.Voyage.newBuilder()
                .setVoyageId(tripId.toString())
                .setOrigin(request.getOrigin())
                .setDestination(request.getDestination())
                .setDeparture(request.getDeparture())
                .setMaximumCapacity(request.getMaximumCapacity())
                .setDescription(request.getDescription())
                .setStatus(TripStatus.Open)
                .setCost(request.getTripCost())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllVoyages(GetAllVoyagesRequest request, StreamObserver<io.icarus7.partage.Voyage> responseObserver) {
        this.voyagesRepository.findAll().stream()
                .map(this::tripMapper)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }


    private io.icarus7.partage.Voyage tripMapper(Voyage voyage) {
        return io.icarus7.partage.Voyage.newBuilder()
                .setVoyageId(voyage.getVoyageId().toString())
                .setOrigin(voyage.getOrigin())
                .setDestination(voyage.getDestination())
                .setDeparture(voyage.getDeparture().getTime())
                .setMaximumCapacity(voyage.getMaximumCapacity())
                .setDescription(voyage.getDescription())
                .setStatus(TripStatus.forNumber(voyage.getStatus().ordinal()))
                .setCost(voyage.getCost().doubleValue())
                .build();
    }

    private Optional<StatusException> isTripValid(AddNewVoyageRequest voyageRequest) {
        var metaData = new Metadata();
        var voyageRequestErrorMessage = Metadata.Key.of("message", Metadata.ASCII_STRING_MARSHALLER);
        if (voyageRequest.getMaximumCapacity() < 0 || voyageRequest.getMaximumCapacity() > 10) {
            metaData.put(voyageRequestErrorMessage, "Capacity of the trip must be more than 0 and less than 11");
            return Optional.of(new StatusException(Status.INVALID_ARGUMENT, metaData));
        }
        if (voyageRequest.getTripCost() < 0) {
            metaData.put(voyageRequestErrorMessage, "The trip cost cannot be negative");
            return Optional.of(new StatusException(Status.INVALID_ARGUMENT, metaData));
        }
        if (voyageRequest.getDescription().length() > 50) {
            metaData.put(voyageRequestErrorMessage, "The description cannot be longer than 50 characters");
            return Optional.of(new StatusException(Status.INVALID_ARGUMENT, metaData));
        }
        return Optional.empty();
    }
}
